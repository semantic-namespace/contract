(ns semantic-namespace.contract.type
  (:require [clojure.spec.alpha :as s]))

(defonce registry (ref {}))

(defn instances
  [id]
  (::instances (id @registry)))

(defn props
  [type]
  (::props (type @registry)))

(defn def
  "adds to the registry a contract type if all of the props exists on clojure.spec/registry "
  [id props]
  (mapv #(assert (% (s/registry)) (format "%s is not a spec yet, do you forget to s/def or to load/eval firstly that other ns?" (pr-str %))) props)
  (when-not (or (nil? (id @registry))
                (= (::props (id @registry)) props)) 
            (throw (ex-info (format "this type is already defined %s" id)
                            {:type (id @registry)
                             :id id
                             :props props})))

  (when-not (= (::props (id @registry)) props)
    (dosync (alter registry assoc id {::props props ::instances #{}})))
  id)

(defn remove [id]
  (dosync (alter registry dissoc id {})))


