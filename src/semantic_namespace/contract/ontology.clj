(ns semantic-namespace.contract.ontology
  (:require [clojure.spec.alpha :as s]))

;; Supports dependency direction axioms, cycle detection, topological ordering invariants.
(def tiers #{:tier/service
             :tier/feature
             :tier/api})
(s/def :tier/type tiers)

;;Cross-Cutting Concerns:: Non-functional identity semantics.
(def cross-cuttings #{:cross-cutting/security
                      :cross-cutting/compliance
                      :cross-cutting/monitoring
                      :cross-cutting/logging})
(s/def :cross-cutting/types cross-cuttings)

;;Integration Role : Semantic integration category.
(def integrations #{:integration/internal
                    :integration/external
                    :integration/cross-domain
                    :integration/adapter})
(s/def :integration/type integrations)

