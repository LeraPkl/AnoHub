rs.initiate()

db = db.getSiblingDB('feed');

db.posts.insertMany([
    {
        _id: "6617b6aa47e4ba383d7b2da9",
        title: "First Post",
        topicId: "66255057519f4662b52fcdc6",
        userProfileId: "fbc8ff97-f4fd-4ee6-bbdc-d0f6c2418c22",
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

db.reposted_posts.insertMany([
    {
        postId: "6617b6aa47e4ba383d7b2da9",
        userProfileId: "9f35c30c-8a15-465e-8027-479d7a70cf96"
    },
]);

db.topics.insertMany([
    {
        _id: "66255057519f4662b52fcdc6",
        categoryId: "6617b6aa47e4ba383d7b2da9",
        name: "cool topic"
    },
]);
