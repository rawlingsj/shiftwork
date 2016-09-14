# How to work with database dumps
You need to have installed [PostgreSQL] and its bin folder should be in your PATH (environment variable).

## How to create dump
In order to create database dump of the schema that you want to backup you need to execute the follwing command:
```sh
$ pg_dump -U <user> <target schema> > <output file>
```
where
* <user> is a name of database user that have access to target schema
* <target schema> is a name of the database schema you want to backup
* <output file> is a name of the file in which you want to store the dump

Note that:
* the command may ask you the password of the user that you specified in <user>
* the generated dump will contain not only data but everything what you had in backed up schema - tables, sequences, indexes, etc. In order to backup only data use --data-only key

Command example for staffservice:
```sh
$ pg_dump -U staffservice staffservice > shiftwork_2016-09-02.dump
```

## How to upload dump

The backed up dump can be applied to existing empty schema using the following command:
```sh
$ psql -U <user> <target schema> < <input file>
```
where
* <user> is a name of database user that have access to target schema
* <target schema> is a name of the database schema for which you want to apply the dump
* <input file> is a name of the dump file

Command example for staffservice:
```sh
$ psql -U staffservice staffservice < shiftwork_2016-09-02.dump
```



[PostgreSQL]: <https://www.postgresql.org/download/>
