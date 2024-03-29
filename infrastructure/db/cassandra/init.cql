CREATE KEYSPACE IF NOT EXISTS user_profiles WITH REPLICATION = {
    'class' : 'SimpleStrategy', 'replication_factor' : 1
    };
