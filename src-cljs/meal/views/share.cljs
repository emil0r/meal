(ns meal.views.share
  (:require [clojure.string :as str]
            [meal.channel :as channel]
            [meal.input :as input]
            [meal.sync :as sync]
            [re-frame.core :refer [dispatch register-handler]]
            [reagent.core :as r]
            [secretary.core :as secretary]
            [taoensso.sente :as sente]))



(defn share-cb [reply]
  (when (sente/cb-success? reply)
    (sync/sync! :meals/fetch nil :meals/handler)
    (secretary/dispatch! (str "/meal/" (:slug reply)))))

(defn handle-share [state [_ name picture ingredients description]]
  (when-let [auth (:auth state)]
    (channel/chsk-send! [:meal/add {:name name
                                    :auth auth
                                    :ingredients ingredients
                                    :description description}]
                        1000
                        share-cb))
  state)

(defn init []
  (register-handler :share/click handle-share))


(defn view []
  (let [name (r/atom "")
        picture (r/atom "")
        ingredients (r/atom "")
        description (r/atom "")]
   (fn []
     [:div
      [:h1 "Share a meal!"]
      [:table.table
       [:tr
        [:th "Name of the meal"]
        [:td [input/text
              :name name
              :class :form-control
              :placeholder "Name of meal"]]]
       [:tr
        [:th "Picture"]
        [:td [input/file
              :picture picture
              :class :form-control
              :accept "image/*;capture=camera"]]]
       [:tr
        [:th]
        [:td [:strong "Ingredients and Description are optional"]]]
       [:tr
        [:th "Ingredients"]
        [:td [input/textarea
              :ingredients ingredients
              :class :form-control
              :placeholder (str/join "\n" ["1 scoop of awesome"
                                           "2 table spoons of delicious"
                                           "1 table"])]]]

       [:tr
        [:th "Description"]
        [:td [input/textarea
              :description description
              :class :form-control
              :placeholder (str/join "\n" ["Take your scoop of awesome and mix it with your spoons of delicious"
                                           "Invite friends"
                                           "Sit down at the table with friends and enjoy"])]]]

       [:tr
        [:th]
        [:td [input/button
              :label "Share!"
              :class "btn btn-primary"
              :on-click #(dispatch [:share/click @name @picture @ingredients @description])]]]]])))
