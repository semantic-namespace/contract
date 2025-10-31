(defproject com.github.semantic-namespace/contract "0.1.1-SNAPSHOT"
  :description "semantic approach to clojure.core/def partially based on clojure.core.spec.alpha/def"
  :url "https://github.com/semantic-spec/contract"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.github.semantic-namespace/spec.keys "0.1.0-SNAPSHOT"]
                 [com.github.semantic-namespace/compound-identity "0.1.1-SNAPSHOT"]]
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :creds :gpg}]]
  :profiles {:dev {:source-paths   ["dev/src"]
                   :repl-options   {:init-ns dev}
                   :dependencies [[org.clojure/tools.namespace "1.4.4"]]}})
