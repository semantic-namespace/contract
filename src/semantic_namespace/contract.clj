(ns semantic-namespace.contract
  (:require [clojure.spec.alpha :as s]
            [clojure.set :as set]
            [semantic-namespace.compound.identity :as compound.identity]))

(defn def
  "register a contract type relation for a specific instance and contract-props"
  [v0 props]
  (let [v (conj v0 :semantic-namespace.contract/type)
        type-keys (let [res (->> (keys @compound.identity/registry)
                                 (filter :semantic-namespace.contract/type)
                                 (filter #(not= v0 (set/difference v %))))]
                    (assert (= 1 (count res)) {v0 res})
                    (let [id3 (first res)]
                      (:semantic-namespace.spec.keys/specs (get @compound.identity/registry id3))))]
    (assert type-keys "type should be defined as contract.type/def before using this contract/def")
    (mapv (fn [type-key]
            (when-not (s/valid? type-key (type-key props))
              (throw (ex-info (format "Invalid data for : %s" type-key)
                              {:spec-explain-data (s/explain-data type-key (type-key props))
                               :spec type-key
                               :spec-value (type-key props)}))))
          type-keys)
    (swap! compound.identity/registry assoc (conj v0 :semantic-namespace.contract/instance) props)
    [(conj v0 :semantic-namespace.contract/instance) type-keys]))

(defn fetch
  [id]
  (get @compound.identity/registry (conj id :semantic-namespace.contract/instance)))
