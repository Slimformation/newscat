(ns newscat.core
  (:use [ring.server.standalone]
        [newscat.app :only [app]])
  (:gen-class))

(defn -main
  [& m]
  (serve app))
