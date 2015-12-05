(ns meal.login)


(defn show-login []
  "<fb:login-button scope='public_profile,email' onlogin='meal.login.handle_login();'></fb:login-button>")

(defn ^:export check-login [response]
  (let [r (js->clj response)]
    ;;(println (get r "status"))
    )
  (.log js/console response)
  (println "yea yea"))


(defn ^:export handle-login [response]
  (.log js/console response))

(defn ^:export handle-logout [response]
  (.log js/console response))

(defn ^:export logout []
  (.logout js/FB handle-logout))
