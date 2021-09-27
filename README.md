# worqplace-Backend | Quintor - Hogeschool Utrecht

## Table of contents

- [Documentation](#documentation)
- [Team](#team)
- [Git Strategy](#git-strategy)
- [Agile](#agile)
- [Coding Standards](#coding-standards)
- [Git](#git)

## Documentation

## Team

This project will be made by a group of seven, each with their different strengths and weaknesses that complement
eachother.

The team has the following members:

- Milan Dol ([@MDol](https://gitlab.com/MDol))
- Said Shirre ([@Bosshi](https://gitlab.com/Bosshi))
- Gerson Mak ([@ItsGers8](https://gitlab.com/ItsGers8))
- Mees Wieman ([@meesvw](https://gitlab.com/meesvw))
- Arutun Avedisyan ([]())
- Jan-paul van der moolen ([@janpaul1999](https://gitlab.com/janpaul1999))
- Daan Docters van Leeuwen ([@D0an](https://gitlab.com/D0an))

## Git strategy

For our git strategy, we are using a modified version of git flow. In our strategy we use the following branches:

Main branch Development branch Feature branches

The only things that will go in the master branch are releases, accompanied by git tag tags (v1, v2, etc.). There is a
release at the end of every iteration.

All the development work will be done in the development branch. This is to ensure that the deployed version (master
branch) will always remain stable.

For every story or (sub)task we create a new feature branch, each team member can do whatever he wants in this branch (
rebasing, force pushing, all of it). These feature branches will be used to make Pull Requests in Github. In these PR's
there will be regular reviews to ensure high code quality.

## Agile

This project will make use of the Agile workflow, implementing the Scrum method. This can be seen from our project
boards. There is a different board for each iteration.

Currently, we use the following lanes:

Backlog (shared between iterations)
To Do In progress Done

We have added all the different types of User Stories located in our backlog, however, each different User Story also
has Sub Tasks related to that particular User Story. This way, we can assign different team members to the sub tasks.

## Coding standards

This paragraph is primarily meant for the team, but it can give insights as to how we're keeping the quality of the code
up.

### Java

To ensure the code quality of Java is high, we will implement certain patterns wherever needed and follow general rules
that help keep our code clear.

We implement design patterns wherever possible, by using the explanations from
the [refactoring.guru](https://refactoring.guru/) website.

Prefer explicitly typing your variable over `var`:

```java
// Don't do this:
var connection = new Connection();

// Instead, do this:
Connection connection = new Connection();
```

Do not add unnecessary comments:

```java
// Don't do this:

/**
 * Creates an object
 */
public Object createObject(){...}

// Do this:
public Object createObject(){...}
```

Remove unnecessary newlines:

```java
// Don't do this:
public class Xyz {

    private int i = 1;

}

// Do this:
public class Xyz {
    private int i = 1;
}
```

Prefer Lombok over standard boilerplate code:

```java
// Don't do this
public class Aircraft {
    private String id;
    private Type type;
    private Flight current;
    private List<Flight> past;
    private Fleet fleet;

    public Aircraft(String id, Type type, Flight current, List<Flight> past, Fleet fleet) {
        this.id = id;
        this.type = type;
        this.current = current;
        this.past = past;
        this.fleet = fleet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    ...
}

// Do this:
@Data
@AllArgsConstructor
public class Aircraft {
    private String id;
    private Type type;
    private Flight current;
    private List<Flight> past;
    private Fleet fleet;
}
```

### Git

This has been discussed many times by other people, thus it is only natural to link a clear and concise article about
this topic:

https://chris.beams.io/posts/git-commit/

The points mentioned in this article are the ones we should be using for making clear git messages.

To keep our git history clean, we don't use the `git merge` command by ourselves, instead, we rebase our `feature`
branches. No unnecessary merges from `development` to `feature/...`, only merges from the PR's in `development`.

