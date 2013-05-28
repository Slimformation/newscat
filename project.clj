(defproject newscat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.2"]
                 [io.curtis/boilerpipe-clj "0.2.0"]
                 [org.clojure/java.jdbc "0.3.0-alpha4"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [org.clojure/data.json "0.2.2"]
                 [clojure-opennlp "0.3.0"]
                 [lib-noir "0.4.6"]
                 [compojure "1.1.5"]
                 [ring-server "0.2.8"]
                 [org.jsoup/jsoup "1.7.2"]
                 [environ "0.4.0"]]
  :plugins [[lein-ring "0.8.5"]
            [lein-environ "0.4.0"]]
  :resource-paths ["resources" "models"]
  :min-lein-version "2.0.0"
  :ring {:handler newscat.app/app}
  :main ^:skip-aot newscat.core)
