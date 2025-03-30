# URL Shortener (WIP: 👷🔧)
> A backend application for shortening URLs using spring boot as backend framework
> and MySQL as a database.

### Swagger docs
- http://localhost:8080/swagger-ui/index.html


## Functional Requirements
> 1. Shorten a URL
>> Custom alias
>>
>> Expiration time
> 
> 2. Retrieve original url if shortened url is provided.
>
> 3. Delete a URL.

### Additional Requirements (Future Scope)
<details>
<summary>Click to expand</summary>

1. Shorten URL with custom alias.

2. Specify expiration date.

3. User Authentication

4. Analytics on clicks

</details>

## Core Entities
- Original URL
- Short URL
- User

## API 
> Create a new shortened URL
>> POST
>
> Get the original url from the shortened URL
>> Get
>
> Update an existing shortened URL
>> PUT
>
> Delete an existing URL
>> DELETE

## Endpoints
> Shorten URL
>> POST /urls
>>
>>{
>>
>>    "long_url": "https://example.com/some/very/veryy/long/url",
>> 
>>    "custom_alias": "optional_custom_alias",
>>
>>    "expiration": "optional_expiration_date",
>> 
>>}
>> -> 201 CREATED
>>{
>>
>>  "short_url": "http://short.ly/abc123"
>>
>>}
>
> Redirect to original URL
>> GET /{short_code}  -> 302 TEMPORARY REDIRECT