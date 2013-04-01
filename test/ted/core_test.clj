(ns ted.core-test
  (:use ted.core
        clojure.test
        midje.sweet)
  (:require [clj-http.client :as client]))

;--------------------<functions>--------------------
(def sleep-time 5000)

(defn not-running []
  (while (not=
           (:body (clj-http.client/get (str "http://localhost:"
                                            ted.core/port-no
                                            "/tasks/count-running")))
           (str 0))
         (. Thread (sleep sleep-time))))

(defn one-task-running []
  (not-running)
  (clj-http.client/get (str "http://localhost:"
                            ted.core/port-no
                            "/tasks/schedule?class=codilime.ted.example.Itemize"))
  (def r (Integer. (:body (clj-http.client/get (str "http://localhost:"
                                          ted.core/port-no
                                          "/tasks/count-running")))))
  (def s (Integer. (:body (clj-http.client/get (str "http://localhost:"
                                          ted.core/port-no
                                          "/tasks/count-scheduled")))))
  (and (= r 1) (= s 0)))

;; for not too big n (it should be possible to send all the requests before the execution of the first one ends)
(def n 100)

(defn scheduling-n-tasks []
  (not-running)
  (dotimes [i n]
    (clj-http.client/get (str "http://localhost:"
                              ted.core/port-no
                              "/tasks/schedule?class=codilime.ted.example.Itemize")))
  (def r (Integer. (:body (clj-http.client/get (str "http://localhost:"
                                                    ted.core/port-no
                                                    "/tasks/count-running")))))
  (def s (Integer. (:body (clj-http.client/get (str "http://localhost:"
                                          ted.core/port-no
                                          "/tasks/count-scheduled")))))
  (+ r s))

(defn checking-zeros []
  (not-running)
  (Integer. (:body (clj-http.client/get (str "http://localhost:"
                                          ted.core/port-no
                                          "/tasks/count-scheduled")))))
;--------------------</functions>--------------------

;----------------------<facts>----------------------
(facts (str "GET results in status "
            ted.core/request-succeeded
            ", while methods other than GET result in status "
            ted.core/wrong-request)

  (fact "GET"    
    (:status (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))) =>  ted.core/request-succeeded)

  (fact "POST"
    (let []
      (try
        (def exc 0)
        (clj-http.client/post (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request)

  (fact "HEAD"
    (let []
      (try
        (def exc 0)
        (clj-http.client/head (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request)

  (fact "PUT"
    (let []
      (try
        (def exc 0)
        (clj-http.client/put (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request)

  (fact "DELETE"
    (let []
      (try
        (def exc 0)
        (clj-http.client/delete (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request)

  (fact "OPTIONS"
    (let []
      (try
        (def exc 0)
        (clj-http.client/options (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request)

  (fact "PATCH"
    (let []
      (try
        (def exc 0)
        (clj-http.client/patch (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request))


(facts (str "uris starting with http://localhost:" ted.core/port-no" /tasks result in "
            ted.core/request-succeeded
            ", while others result in "
            ted.core/wrong-request)

  (fact "tasks/count-running"    
    (:status (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))) =>  ted.core/request-succeeded)

  (fact "tasks/count-scheduled"
    (:status (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-scheduled"))) => ted.core/request-succeeded)

  (fact "foo/foo2"
    (let []
      (try
        (def exc 0)
        (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/foo/foo2"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request))


(facts "field of a query (tasks/schedule) string has to be called 'class'"

  (fact "tasks/schedule?class=codilime.ted.example.Itemize"
    (:status (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/schedule?class=codilime.ted.example.Itemize"))) => ted.core/request-succeeded)

  (fact "tasks/schedule?foo=foo2"
    (let []
      (try
        (def exc 0)
        (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/schedule?foo=foo2"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/wrong-request))


(facts "value of a 'class' field has to be a fully qualified name of a class (implementing Runnable interface) existing on the project's classpath"

  (fact "tasks/schedule?class=codilime.ted.example.Itemize"
    (:status (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/schedule?class=codilime.ted.example.Itemize"))) => ted.core/request-succeeded)

  (fact "tasks/schedule?class=foo.foo2.NonExistingClass"
    (let []
      (try
        (def exc 0)
        (clj-http.client/get (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/schedule?class=foo.foo2.NonExistingClass"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (:status (:object (.data exc)))) => ted.core/class-not-found))


(facts "scheduling a task results in incrementing the 'scheduled' counter or, if there aren't too many running tasks, indirectly the 'running' counter; counters 'scheduled' and 'running' values are always 0 or higher"

  (fact "one task running"
    (one-task-running) => true)

  (fact "n tasks scheduled"
    (scheduling-n-tasks) => n)

  (fact "if running-count == 0 then scheduled-count == 0"
    (checking-zeros) => 0))
;----------------------</facts>----------------------
