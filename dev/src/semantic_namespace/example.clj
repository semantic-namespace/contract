(ns semantic-namespace.example
  (:require [semantic-namespace.contract :as contract]
            [clojure.spec.alpha :as s]
            [semantic-namespace.contract.type :as contract.type]))

(s/def :docs/content string?)

(contract.type/def :semantic-namespace/docs [:docs/content])
;; => :semantic-namespace/docs

(contract/def
  :semantic-namespace/docs
  :app.cool/stuff
  {:docs/content "Hello app cool stuff docs!"})
;; => [:semantic-namespace/docs :app.cool/stuff]

(contract/fetch :semantic-namespace/docs :app.cool/stuff)
;; {:semantic-namespace.contract/type :semantic-namespace/docs,
;;  :semantic-namespace.contract/instance :app.cool/stuff,
;;  :docs/content "Hello app cool stuff docs!"}


(contract.type/instances  :semantic-namespace/docs)
;;#{:app.cool/stuff}

(contract.type/props :semantic-namespace/docs)
;; [:docs/content]

(mapv (partial contract/fetch :semantic-namespace/docs)
      (contract.type/instances :semantic-namespace/docs))
;; [{:semantic-namespace.contract/type :semantic-namespace/docs,
;;   :semantic-namespace.contract/instance :app.cool/stuff,
;;   :docs/content "Hello app cool stuff docs!"}]


(contract/def
  :semantic-namespace/docs
  :app.feature/foo
  {:docs/content "app feature foo  docs!"})
;;[:semantic-namespace/docs :app.feature/foo]

(mapv (partial contract/fetch :semantic-namespace/docs) (contract.type/instances :semantic-namespace/docs))
;; [{:semantic-namespace.contract/type :semantic-namespace/docs,
;;   :semantic-namespace.contract/instance :app.cool/stuff,
;;   :docs/content "Hello app cool stuff docs!"}
;;  {:semantic-namespace.contract/type :semantic-namespace/docs,
;;   :semantic-namespace.contract/instance :app.feature/foo,
;;   :docs/content "app feature foo  docs!"}]


