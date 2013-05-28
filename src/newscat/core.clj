(ns newscat.core
  (:use [ring.server.standalone]
        [newscat.app :only [app]])
  (:gen-class))

(defn -main
  [port]
  (serve app {:port (Integer. port)}))
