(ns imkn-server.db.creator
  (:require [clojure.java.jdbc :as sql])
  (:use
    [korma.db]
    [imkn-server.db.utils]
    [clojure.tools.logging :only [info]]))

(defn- create-posts-table-query []
  (sql/create-table-ddl :post
                        [[:id "bigint primary key auto_increment"]
                         [:title "varchar"]
                         [:text "longvarchar"]
                         [:date "timestamp default CURRENT_TIMESTAMP()"]]))

(defn- create-comment-table-query []
  (sql/create-table-ddl :comment
                        [[:id "bigint primary key auto_increment"]
                         [:user "varchar"]
                         [:text "longvarchar"]
                         [:post_id "bigint references post (id)"]
                         [:date "timestamp default CURRENT_TIMESTAMP()"]]))

(defn- create-post-item []
  (sql/insert! db-spec :post
               {:title "Власти установили личность устроившего теракт в клубе в Стамбуле"
                :text  "Глава МИД Турции Мевлют ​Чавушоглу заявил, что власти страны установили личность человека, который устроил теракт в ночном клубе Стамбула в новогоднюю ночь. Об этом он заявил на брифинге, передает агентство Anadolu. При этом министр отказался назвать имя злоумышленника. Ранее агентство сообщило о задержании в Измире еще пятерых подозреваемых, причастных к атаке. Еще восемь человек, подозреваемых в причастности к преступлению, были задержаны 2 января. Власти заявили, что число подозреваемых может возрасти, пока идет следствие по делу о теракте."}))

(defn create-tables []
  (when-not (exists? db-spec "post")
    (sql/execute! db-spec (create-posts-table-query)))
  (when-not (exists? db-spec "comment")
    (sql/execute! db-spec (create-comment-table-query)))
  nil)