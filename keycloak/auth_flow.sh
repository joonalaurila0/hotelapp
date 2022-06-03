#!/bin/sh
# Auth flow of authorization code grant type
#
# Upon requesting authorization, a short-lived authorization code is returned, 
# which can be used to obtain the access token.

# o  Authorization endpoint - used by the client to obtain
#    authorization from the resource owner via user-agent redirection.

# o  Token endpoint - used by the client to exchange an authorization
#    grant for an access token, typically with client authentication.


auth_url="localhost:8080/realms/master/protocol/openid-connect/auth?client_id=hotelapp&client_secret=zjpSKq1HEj2RRqiIpb&grant_type=authorization_code&response_type=code"
login_auth_code="http://localhost:8080/realms/master/login-actions/authenticate"

# Provides login page
curl -v $auth_url

# Logs in, providing the authorization grant, code
#
# When using the Authorization Code Flow, the Authorization Response MUST return the parameters defined in Section 4.1.2 of OAuth 2.0 [RFC6749] 
# by adding them as query parameters to the redirect_uri specified in the Authorization Request using the application/x-www-form-urlencoded format

curl -v -X POST $url -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=$username" \
    -d "password=$password" \
    -d "session_code="
    -d "grant_type=authorization_code" \
    -d "code=$code"
    -d "redirect_uri=http://localhost:8081"
    -d "client_id=hotelapp-client" \
    -d "client_secret=$secret"

# With this code, now it is possible to exchange the authorization grant for an access token.
# https://openid.net/specs/openid-connect-core-1_0.html#TokenEndpoint

curl -v -X POST $url -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=" \
  -d "redirect_uri=" \
  -d "grant_type=authorization_code"

# Response should look like this:
#
#   HTTP/1.1 200 OK
#   Content-Type: application/json
#   Cache-Control: no-store
#   Pragma: no-cache
# 
#   {
#    "access_token": "SlAV32hkKG",
#    "token_type": "Bearer",
#    "refresh_token": "8xLOxBtZp8",
#    "expires_in": 3600,
#    "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFlOWdkazcifQ.ewogImlzc
#      yI6ICJodHRwOi8vc2VydmVyLmV4YW1wbGUuY29tIiwKICJzdWIiOiAiMjQ4Mjg5
#      NzYxMDAxIiwKICJhdWQiOiAiczZCaGRSa3F0MyIsCiAibm9uY2UiOiAibi0wUzZ
#      fV3pBMk1qIiwKICJleHAiOiAxMzExMjgxOTcwLAogImlhdCI6IDEzMTEyODA5Nz
#      AKfQ.ggW8hZ1EuVLuxNuuIJKX_V8a_OMXzR0EHR9R6jgdqrOOF4daGU96Sr_P6q
#      Jp6IcmD3HP99Obi1PRs-cwh3LO-p146waJ8IhehcwL7F09JdijmBqkvPeB2T9CJ
#      NqeGpe-gccMg4vfKjkM8FcGvnzZUN4_KSP0aAp1tOJ1zZwgjxqGByKHiOtX7Tpd
#      QyHE5lcMiKPXfEIQILVq0pc_E2DzL7emopWoaoZTF_m0_N0YzFC6g6EJbOEoRoS
#      K5hoDalrcvRYLSrQAZZKflyuVCyixEoV9GfNQC3_osjzw2PAithfubEEBLuVVk4
#      XUVrWOLrLl0nx7RkKU8NXNHq-rvKMzqg"
#   }
