(ns meal.views.navbar)


(defn navbar []
  [:nav.navbar
   [:ul.navbar
    (for [[url name] [["#/login" "Login"]]]
      [:li [:a {:href url} name]])]])
