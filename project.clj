(defproject ted "1.0.0-SNAPSHOT"
  :description "An internship recrutation project."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [ring "1.1.8"]]	
  :java-source-paths ["src"]
  :profiles {:dev
              {:dependencies [[midje "1.4.0"]]
	       :plugins [[lein-midje "2.0.1"]]}}
  :main ted.core
  :min-lein-version "2.0.0")
