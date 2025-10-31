(ns semantic-namespace.contract.type
  (:require [clojure.spec.alpha :as s]
            [semantic-namespace.compound.identity :as compound.identity]
            [semantic-namespace.spec.keys :as spec.keys]))

(defn reset*! []
  (reset! compound.identity/registry {}))

(defn instances
  [id]
  (mapv #(disj (set %) :semantic-namespace.contract/instance) (filter (comp (partial not= (conj id :semantic-namespace.contract/type)) set) (compound.identity/query id)))
)

(defn props
  [id]
  (get @compound.identity/registry (conj id :semantic-namespace.contract/type)))

(defn exists? [id props]
  (compound.identity/exists? (conj id :semantic-namespace.contract/type) props))

(defn def
  "adds to the registry a contract type if all of the props exists on clojure.spec/registry "
  [id0 props & [force?]]
  (mapv #(assert (% (s/registry)) (format "%s is not a spec yet, do you forget to s/def or to load/eval firstly that other ns?" (pr-str %))) props)
  (let [id (conj id0 :semantic-namespace.contract/type)
        {:keys [exists matches]} (or (exists? id props) {})]
    (when (and exists matches)
      (when-not force?
        (throw (ex-info (format "this type is already defined %s" id0)
                        {:explain "You are trying to eval same type with different props on different part of our code, that's not possible. This library is about static semantic specifications that are loaded dynamically on eval, but they never change once loaded"
                         :id id0
                         :props props}))))
    (spec.keys/def id props))
  [id0 props])

(defn remove [id]
  (compound.identity/remove* (conj id :semantic-namespace.contract/type)))


