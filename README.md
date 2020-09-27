# Kafka
All exercises and source code from kafka course on udemy

# All Kafka CMD for Windows:

# Start zookeeper and kafka server
zookeeper-server-start.bat config\zookeeper.properties
kafka-server-start.bat config\server.properties

# Create topic
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1
// first_topic = tên của topic, tên này là unique, identity
// --partitions 3  = chỉnh số partitions, không nhập sẽ báo lỗi, số partitions là tùy ý. 
// --replication-factor 1 = chỉnh số replication , không nhập sẽ báo lỗi, nếu chỉ chạy 1 broker thì nên nhập 1
# List
kafka-topics.sh --zookeeper 127.0.0.1:2181 --list
# Detail
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic first_topic --describe
# Delete
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic first_topic --delete

# Vào console producer
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic first_topic
// sau khi vào mode, có thể gõ bất kỳ message nào, cứ enter 1 lần, thì message sẽ được gửi đi. 
# Truyền property
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic first_topic --producer-property acks=all
// Trường hợp nhập tên 1 topic mới, mà không được tạo trước đó, thì sẽ tự động tạo tạo topic vừa nhập (có WARN cảnh báo). 
// Topic vừa nhập, có các thông số như partition, replication được chỉnh default như ở trong file config/server.properties

# Vào console consumer
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic
// Như command trên, không nhập groupID, thì cli sẽ tự động tạo ra 1 groupId mới, unique
// Để show ra các message từ lúc begin (chưa được "mark as read"), thì truyền thêm param --from-beginning
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --from-beginning

# Để set groupId cho consumer 
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application
// trường hợp các consumer cùng chung group, thì các consumer sẽ được nhận message từ topic 1 cách lần lượt, (round robin)
// trường hợp các consumer khác group, thì với mỗi 1 message, tất cả các consumer đều nhận được message như nhau.

// khi consumer đã nhận được message, thì offset trên mỗi partition được "mark as read" sẽ thay đổi (offset lên giá trị gần nhất)
# Sử dụng reset offset, để khi consumer load lại, có thể call lại các message từ offset chưa được "mark"
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic first_topic
// còn các option khác như:
 --to-datetime
 --by-period
 --to-earliest
 --to-latest
 --shift-by
 --from-file
 --to-current
