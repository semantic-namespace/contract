(ns semantic-namespace.endpoint-example
  (:require [clojure.spec.alpha :as s]
            [semantic-namespace.contract :as contract]
            [semantic-namespace.contract.type :as contract.type]))

(s/def :endpoint/handler fn?)
(s/def :endpoint/param s/get-spec)
(s/def :endpoint/params (s/coll-of :endpoint/param))
(s/valid? :endpoint/param :endpoint/handler)
(s/def :authz/role #{:user :admin :client})

(contract.type/def :semantic-namespace/endpoint [:endpoint/handler :endpoint/params :authz/role])

(s/def :user/id uuid?)
(s/def :admin/id uuid?)

(contract/def :semantic-namespace/endpoint :my-app/user
  {:endpoint/handler (fn [req] (format "welcome user! %s" (:user/id req)) )
   :endpoint/params [:user/id]   
   :authz/role :user})

(contract/def :semantic-namespace/endpoint :my-app.admin/user
  {:endpoint/handler (fn [req] (format "admin %s for user %s" (:admin/id req) (:user/id req)) )
   :endpoint/params [:user/id :admin/id]   
   :authz/role :admin})

;; routing part 
(contract.type/instances :semantic-namespace/endpoint)
;; #{:my-app.admin/user :my-app/user}

(def admins #{#uuid "a375f226-8ed8-418a-848d-3913b7b60550"})

;; eg receiving request for admin user
(let [request {:admin/id #uuid "a375f226-8ed8-418a-848d-3913b7b60550"
               :user/id #uuid "eb97a1f8-ca71-4eba-953d-f340d4fe36c5"}
      endpoint-contract (contract/fetch :semantic-namespace/endpoint :my-app.admin/user)]
  ;; assert s/valid? params
  (mapv #(assert (s/valid? % (% request))) (:endpoint/params endpoint-contract))
  (when (:endpoint/admin endpoint-contract) 
    (assert (contains? admins (:admin/id request))))
  ((:endpoint/handler endpoint-contract) request))

;; best part: semantic introspection 
(->> (contract.type/instances :semantic-namespace/endpoint)
     (mapv (partial contract/fetch :semantic-namespace/endpoint))
     (group-by :authz/role)
     )

(->> (contract.type/instances :semantic-namespace/endpoint)
     (mapv (partial contract/fetch :semantic-namespace/endpoint))
     (group-by :endpoint/params)
     )

(->> (contract.type/instances :semantic-namespace/endpoint)
     (mapv (partial contract/fetch :semantic-namespace/endpoint))     
     (filter (comp #(contains? % :admin/id) set :endpoint/params))
     )

