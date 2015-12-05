(ns meal.views.index
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [ez-web.core :refer [paginate]]
            [re-frame.core :refer [subscribe register-sub]]))


(defn get-meals-info [db _]
  (reaction {:meals (get-in @db [:meal/meals] [])
             :count (get-in @db [:meal/count] 0)
             :page (get-in @db [:view/index] 1)}))
(register-sub :meals/info get-meals-info)

(defn- show-meal [{:keys [name slug description image]}]
  [:div.row {:key slug}
   [:div.col-md-12
    [:a {:href (str "#meal/" slug)}
     [:h2 name]
     [:p.info description]]]])

(defn paginator [page pp count]
  (let [{:keys [next prev]} (paginate count pp page)]
    [:div.pagination
     (if prev
       [:a.prev {:href (str "#index/" prev)} "prev"]
       [:span.prev "prev"])
     [:span.page "page " page]
     (if next
       [:a.next {:href (str "#index/" next)} "next"]
       [:span.next "next"])]))

(defn view []
  (let [meals-info (subscribe [:meals/info])]
    (fn []
      [:div
       [:h1 "Meals"]
       (map show-meal (:meals @meals-info))
       (paginator (:page @meals-info) 1 (:count @meals-info))])))
