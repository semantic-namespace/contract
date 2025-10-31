(ns semantic-namespace.contract.utils
    (:require [semantic-namespace.compound.identity :as identity]))

(defn contract-types
  "Find all contract type definitions."
  []
  (->> (identity/all-identities)
       (filter #(contains? % :semantic-namespace.contract/type))))

(defn contract-instances
  "Find all contract instances."
  []
  (->> (identity/all-identities)
       (filter #(contains? % :semantic-namespace.contract/instance))))





