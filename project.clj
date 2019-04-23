(defproject sietsma-site "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [stasis "2.4.0"]
                 [ring "1.7.1"]
                 [optimus "0.20.2"]
                 [optimus-sass "0.0.3"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.6"]
                 [clygments "2.0.0"]
                 [me.raynes/cegdown "0.1.1"]
                 [hiccup "1.0.5"]
                 [frontmatter "0.0.1"]]
  :ring {:handler sietsma-site.web/app}
  :aliases {"build-site" ["run" "-m" "sietsma-site.web/export"]}
  :profiles {:dev {:plugins [[lein-ring "0.12.5"]]}})
