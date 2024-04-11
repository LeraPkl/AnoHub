rs.initiate()

db = db.getSiblingDB('feed');

db.posts.insertMany([
    {
        title: "First Post",
        topicId: "topic1",
        userId: 123,
        content: "This is the first post content",
        privacyLevel: "PUBLIC",
        createdAt: new Date(),
        popularity: 0.8
    },
]);

db.post_likes.insertMany([
    {
        postId: "post1",
        userId: 123,
        isLike: true
    },
]);

db.comments.insertMany([
    {
        postId: "post1",
        userId: "user1",
        content: "Great post!",
        createdAt: new Date()
    },
]);
