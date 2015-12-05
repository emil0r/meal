(ns meal.views.index
  (:require [hiccup.page :refer [html5 include-css]]
            [ring.util.http-response :refer [ok]]))


(defn index [request]
  (ok (html5
       [:head
        [:meta {:charset "UTF-8"}]
        [:title "Share a meal with me!"]
        [:link {:rel "icon" :type "image/png" :href "/static/img/meal.png"}]
        (map include-css ["/static/css/main.css"])
        [:script {:type "text/javascript"
                  :src "/static/js/fb.js"}]]

       [:body
        #_"<script>
  window.fbAsyncInit = function() {
    FB.init({
      appId      : '846939068738774',
      xfbml      : true,
      version    : 'v2.5'
    });

    FB.getLoginStatus(function(response) {
        statusChangeCallback(response);
    });
  };

  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = '//connect.facebook.net/en_US/sdk.js';
     fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));
</script>"
        ;;"<fb:login-button scope='public_profile,email' onlogin='checkLoginState ();'></fb:login-button>"
        ;;[:div#status]
        [:noscript "You need javascript enabled"]
        [:div#app {:style "height: 100%;"}]
        [:script {:type "text/javascript"
                  :src "/js/compiled/meal.js"}]])))
