(ns ted.core-test
  (:use ted.core
        clojure.test
        midje.sweet)
  (:require [clj-http.client :as client]))

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
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request))

  (fact "HEAD"
    (let []
      (try
        (def exc 0)
        (clj-http.client/head (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request))

  (fact "PUT"
    (let []
      (try
        (def exc 0)
        (clj-http.client/put (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request))

  (fact "DELETE"
    (let []
      (try
        (def exc 0)
        (clj-http.client/delete (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request))

  (fact "OPTIONS"
    (let []
      (try
        (def exc 0)
        (clj-http.client/options (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request))

  (fact "PATCH"
    (let []
      (try
        (def exc 0)
        (clj-http.client/patch (str "http://localhost:"
                                       ted.core/port-no
                                       "/tasks/count-running"))
        (catch clojure.lang.ExceptionInfo e (def exc e)))
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request)))


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
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request)))

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
      (.getMessage exc)) => (str "clj-http: status " ted.core/wrong-request))

)


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
      (.getMessage exc)) => (str "clj-http: status " ted.core/class-not-found)))
