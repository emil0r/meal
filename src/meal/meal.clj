(ns meal.meal
  (:require [buddy.core.codecs :as codecs]
            [meal.database :as db]
            [yesql.core :refer [defqueries]]))


(defqueries "queries/meal.sql")


(defn get-meals []
  (db/query sql-get-meals))

(defn get-meal [id]
  (first (db/query sql-get-meal {:id id})))

(defn add-meal<! [{:keys [user_id name description ingredients] :as data}]
  (db/query<! sql-add-meal<! (assoc data :key (codecs/str->base64 (str (java.util.UUID/randomUUID))))))
