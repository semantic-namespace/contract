(ns semantic-namespace.contract
  (:require [clojure.spec.alpha :as s]
            [semantic-namespace.contract.type :as contract.type]))

(defonce registry (ref {}))

(defn def
  "register a contract type relation for a specific instance and contract-props"
  [type instance props]
  (let [data {:semantic-namespace.contract/type type
              :semantic-namespace.contract/instance instance
              :semantic-namespace.contract/props props}
        type-keys (contract.type/props type)]
    (assert (type @contract.type/registry) "type should be defined as contract.type/def before using this contract/def")
    (mapv (fn [type-key]
            (when-not (s/valid? type-key (type-key props))
              (throw (ex-info (format "Invalid data for : %s" type-key)
                              {:spec-explain-data (s/explain-data type-key (type-key props))
                               :spec type-key
                               :spec-value (type-key props)}))))
          type-keys)
    (dosync
     (alter registry update instance merge (merge (select-keys data [:semantic-namespace.contract/type
                                                                     :semantic-namespace.contract/instance]) props))
     (alter contract.type/registry update-in [type ::contract.type/instances] conj instance)
     data)
    [type instance]))

(defn fetch
  [type instance]
  (let [contract (instance @registry )]
    (merge
     (select-keys contract [:semantic-namespace.contract/type :semantic-namespace.contract/instance])
     (select-keys contract (contract.type/props type)))))
