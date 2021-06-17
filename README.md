# The back-end app for toko.kz project

The back-end application for toko.kz project

## Deployment
 
To run application locally launch next commands:

```bash
docker run \
      --name toko_db_detached  \
      --net=host \
      --publish 5432:5432 \
      --env POSTGRES_DB='toko_db' \
      --env POSTGRES_USER='toko_app_user' \
      --env POSTGRES_PASSWORD='passw0rd' \
      --detach \
      postgres
docker run \
      --name toko_app \
      --net=host \
      --detach \
      daurendelmukhambetov/toko:0.1.2
```
