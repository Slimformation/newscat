(ns newscat.core
  "Main entrypoint namespace for the executable. Starts serving
  web requests"
  (:use [ring.server.standalone]
        [newscat.app :only [app]])
  (:gen-class))

(defn -main
  "Main function for the entire project"
  [port]
  (serve app {:port (Integer. port)}))
