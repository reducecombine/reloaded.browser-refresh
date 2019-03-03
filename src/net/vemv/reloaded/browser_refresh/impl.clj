(ns net.vemv.reloaded.browser-refresh.impl
  (:require
   [compojure.core :as compojure]
   [net.vemv.reloaded.browser-refresh.impl.script :as script]
   [ring.middleware.params])
  (:import
   (java.util Date UUID)))

(defn moment []
  (-> (Date.) .getTime))

(def last-modified (atom (moment)))

(defn watch-until [reference pred timeout-ms]
  {:post [(boolean? %)]}
  (let [result    (promise)
        watch-key (-> (UUID/randomUUID) str)]
    (try
      (add-watch reference
                 watch-key
                 (fn [_ _ _ value]
                   (when (pred value)
                     (deliver result true))))
      (or (pred @reference)
          (deref result timeout-ms false))
      (finally
        (remove-watch reference watch-key)))))

(defn source-changed-route [refresh-time]
  (compojure/GET "/____source_changed" [^String since]
    (let [since   (Long. since)
          timeout (- 60000 refresh-time)
          result  (watch-until last-modified
                               (fn [^Long last-modified]
                                 (> last-modified since))
                               timeout)]
      (str result))))

(defn wrap-refresh
  [handler refresh-time]
  {:pre [handler (integer? refresh-time)]}
  (reset! last-modified (moment))
  (->> (script/refresh-script refresh-time)
       (script/wrap-with-script handler)
       (compojure/routes (source-changed-route refresh-time))
       (ring.middleware.params/wrap-params)))
