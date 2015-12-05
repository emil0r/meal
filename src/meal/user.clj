(ns meal.user
  (:refer-clojure :exclude [get])
  (:require [meal.database :as db]
            [yesql.core :refer [defqueries]]))


(defqueries "queries/users.sql")

(defn oauth-id [id]
  (str "fb" id))

(defn exists? [id]
  (-> (db/query sql-get-user {:oauth (oauth-id id)})
      count
      zero?
      not))

(defn get [id]
  (first (db/query sql-get-user {:oauth (oauth-id id)})))

(defn create! [name id]
  (db/query! sql-create-user! {:name name :oauth (oauth-id id)}))
