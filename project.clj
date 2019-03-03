(defproject net.vemv/reloaded.browser-refresh "0.3.0"
  :description "Ring middleware for automatically refreshing HTML pages"
  :url "https://github.com/reducecombine/reloaded.browser-refresh"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[comb "0.1.1"]
                 [com.stuartsierra/component "0.4.0"]
                 [compojure "1.6.1"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.namespace "0.3.0-alpha4"]]
  :profiles {:dev {:global-vars  {*warn-on-reflection* true}
                   :dependencies [[medley "1.1.0"]]}})
