# UserCenter

> A easy login/register/update user center.

> you can use it to finish register or login or update.

> You have to tell me that you need to use it
and I need to apply for a project id for you.

> Or you can use the code build your own user center.

> There are two ways to use this project.
- use token(need save in header)
- use cookie(need CORS)

> I can provide the log when you need it

# How to use it

1. go http://47.94.248.38:7200/swagger-ui.html(Shortly after the domain name Will update)
2. use /facade/project input your project name it will return:
```
"data": {
    "projectId": 1,
    "key": "5c175866c8"
  }
```
3. please save projectId and key in your db or cache
4. use /token/register to register，at least you input
```
{
  "password": "123456",
  "projectId": 1,
  "remember": false,
  "username": "hawk"
}
key: 5c175866c8
```
it will return
```
"data": "c970209b8df0419c6712cb96f1fbc58dde9bedd233f3e79c069b4be1eeaafee4"
```   
5. you can get user information by token, please use /facade/token   
you need input your token and key

                              
