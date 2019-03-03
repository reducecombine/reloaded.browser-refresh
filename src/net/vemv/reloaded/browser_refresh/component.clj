(ns net.vemv.reloaded.browser-refresh.component
  (:require
   [com.stuartsierra.component :as component]
   [net.vemv.reloaded.browser-refresh.impl :as impl]))

(defn start [this]
  (assoc this ::wrap-fn impl/wrap-refresh))

(defn stop [this]
  {})

(defn new
  "With this component, HTML responses will be injected a JS script
  which automatically refreshes the browser as you issue a `reset`.

  Only successful responses from GET requests will be modified."
  []
  ^{`component/start start
    `component/stop  stop}
  {})
