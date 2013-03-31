(ns ted.core
  (:use ring.adapter.jetty
        ring.util.response
        ring.middleware.params))

(def scheduled 0)
(def running 0)

(defn scheduling []
 (def scheduled
   (+ scheduled 1)))

(defn started []
  (def scheduled
    (- scheduled 1))
  (def running
    (+ running 1)))

(defn finished []
  (def running
    (- running 1)))

(def item nil)

(defn planning []
  (scheduling)
  (def a (agent item))
  (send a (fn[x]
            (started)
            (.run x)
            (finished))))

(def response-status 200)

(defn handler [request]
  (if (= (:request-method request) :get)
    (if (= (:uri request) "/tasks/schedule")
      (let [s (request :query-string)]
        (let [[a b c] (re-matches #"(.*)=(.*)"
                        (request :query-string))]
          (if (= b "class")
            (let []
              (def response-status 200)
              (try (def item 
                     (-> c 
                       (Class/forName)
                       (. getDeclaredConstructor nil)
                       (. newInstance nil)))
                   (planning)
                (catch ClassNotFoundException e (def response-status 405)))
                {:status response-status})
            ;b != "class"
            {:status 400})))
      (if (= (:uri request) "/tasks/count-running")
         {:status 200
          :body (str running)}
         (if (= (:uri request) "/tasks/count-scheduled")
            {:status 200
             :body (str scheduled)}
            {:status 400})))
    {:status 400}))

(defn app [request]
  (println "-------------------------------")
  (println "Incoming Request:")
  (println request)
  (let [response (handler request)]
    (println "Outgoing Response Map:")
    (println response)
    (println "-------------------------------")
    response))

(defn -main [& args]
  (run-jetty app {:port 8330 :join? false}))
