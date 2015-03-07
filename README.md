Workout
==========================

Giving Neo4j 2.2 a workout with Gatling 2.1

Install Neo4j.

Create some data.

    WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
    FOREACH (r IN range(0,100000) | CREATE (:User {username:names[r % size(names)]+r}))

    MATCH (u1:User),(u2:User)
    WITH u1,u2
    LIMIT 5000000
    WHERE rand() < 0.1
    CREATE (u1)-[:FRIENDS]->(u2);

Test (Run Engine)

Add an index.

    CREATE INDEX ON :User(username)

Test again

See [blog post](http://wp.me/p26jdv-Ja) for more details.