(ns net.vemv.reloaded.browser-refresh.impl.script
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [comb.template :as template]))

(def refresh-script
  (template/fn [refresh-time]
    (slurp (io/resource "ring/js/refresh.js"))))

(defprotocol AsString
  (as-str [x]))

(extend-protocol AsString
  String
  (as-str [s]
    s)

  java.io.File
  (as-str [f]
    (slurp f))

  java.io.InputStream
  (as-str [i]
    (slurp i))

  clojure.lang.ISeq
  (as-str [xs]
    (apply str xs))

  nil
  (as-str [_]
    nil))

(defn add-script [body script]
  (if-let [body-str (as-str body)]
    (string/replace body-str
                    #"<head\s*[^>]*>"
                    #(str % "<script type=\"text/javascript\">" script "</script>"))))

(defn wrap-with-script [handler script]
  (letfn [(get-request? [{:keys [request-method]}]
            (= request-method :get))

          (success? [{:keys [status]}]
            (<= 200 status 299))

          (html-content? [response]
            (if-let [content-type (get-in response [:headers "Content-Type"])]
              (re-find #"text/html" content-type)))]

    (fn [request]
      (let [response (handler request)]
        (if-not (and (get-request? request)
                     (success? response)
                     (html-content? response))
          response
          (-> response
              (update-in [:body] add-script script)
              (update-in [:headers] dissoc "Content-Length")))))))
