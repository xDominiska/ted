(defproject ted "1.0.0-SNAPSHOT"
  :description "An internship recrutation project."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.1.8"]]	
  :java-source-paths ["src"]
  :profiles {:dev
              {:dependencies [[midje "1.4.0"]
                              [clj-http "0.7.0"]]
	       :plugins [[lein-midje "2.0.1"]]}}
  :main ted.core
  :test-path "test"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"})
