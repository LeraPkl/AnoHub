rs.initiate()

db = db.getSiblingDB('feed');

db.posts.insertMany([
    {
        _id: "6617b6aa47e4ba383d7b2da9",
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
        postId: "6617b6aa47e4ba383d7b2da9",
        userId: 123,
        isLike: true
    },
]);

db.comments.insertMany([
    {
        postId: "6617b6aa47e4ba383d7b2da9",
        userId: "user1",
        content: "Great post!",
        createdAt: new Date()
    },
]);
