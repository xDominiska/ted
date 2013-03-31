(ns ted.core
  (:use ring.adapter.jetty
        ring.util.response
        ring.middleware.params))

(def response-status 200)
(def request-succeeded 200)
(def wrong-request 400)
(def class-not-found 405)

(def port-no 8330)

(def item nil)

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

(defn planning []
  (scheduling)
  (def a (agent item))
  (send a (fn[x]
            (started)
            (.run x)
            (finished))))

(defn handler [request]
  (if (= (:request-method request) :get)
    (if (= (:uri request) "/tasks/schedule")
      (let [s (request :query-string)]
        (let [[a b c] (re-matches #"(.*)=(.*)"
                        (request :query-string))]
          (if (= b "class")
            (let []
              (def response-status request-succeeded)
              (try (def item 
                     (-> c 
                       (Class/forName)
                       (. getDeclaredConstructor nil)
                       (. newInstance nil)))
                   (planning)
                (catch ClassNotFoundException e (def response-status class-not-found)))
                {:status response-status})
            ;b != "class"
            {:status wrong-request})))
      (if (= (:uri request) "/tasks/count-running")
         {:status request-succeeded
          :body (str running)}
         (if (= (:uri request) "/tasks/count-scheduled")
            {:status request-succeeded
             :body (str scheduled)}
            {:status wrong-request})))
    {:status wrong-request}))

(defn app [request]
  (handler request))

(defn -main [& args]
  (run-jetty app {:port port-no}))
