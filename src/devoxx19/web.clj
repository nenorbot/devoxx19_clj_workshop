(ns devoxx19.web
  (:require [org.httpkit.server :as s]
            [compojure.core :refer [routes GET]]))


(defn handler [req]
  {:status 200
   :body "<h1><b>Hello, Clojure</b></h1>"})

(def server (atom nil))

(defn start-server []
  (reset! server (s/run-server handler {:port 8888})))

(defn stop-server []
  (swap! server (fn [s]
                  (when s
                    ;; the server is also a function. Invoking it stops it
                    (s)))))

(defn reload []
  (stop-server)
  (start-server))

(def handler
  (routes
   (GET "/" []
        {:status 200 :body "<h1><b>Hello, Clojure!</b></h1>"})
   (GET "/user/:user-name" [user-name :as req]
        {:status 200 :body (str "<h1><b>Hello, " user-name "</b></h1>")})))









