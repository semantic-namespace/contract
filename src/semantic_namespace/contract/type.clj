(ns semantic-namespace.contract.type
  (:require [clojure.spec.alpha :as s]))

(defonce registry (ref {}))

(defn instances
  [{:keys [::id]}]
  (::instances (id @registry)))

(defn def
  "adds to the registry a contract type if all of the props exists on clojure.spec/registry "
  [id props]
  (mapv #(assert (% (s/registry)) (format "%s is not a spec yet, do you forget to s/def or to load/eval firstly that other ns?" (pr-str %))) props)
  (dosync (alter registry assoc id {::props props ::instances #{}}))
  id)
