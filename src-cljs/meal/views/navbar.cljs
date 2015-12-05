(ns meal.views.navbar)

(defn sidebar []
  [:ul.sidebar
   [:li [:a {:href "#index"} "Main"]]
   (for [[url name] [["#share" "Share!"]]]
     [:li [:a {:href url} name]])])
