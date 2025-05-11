source ./tokens.txt
response=$(curl \
	-H 'Content-Type: application/json' \
	-H "Authorization: Bearer $REFRESH" \
	--request POST \
	http://localhost:8080/session/refresh)

# show the response
echo $response | jq

# extract and save the tokens
refresh_token=$(echo $response | jq -r '.refresh_token') && echo "REFRESH=$refresh_token" > tokens.txt
access_token=$(echo $response | jq -r '.access_token') && echo "ACCESS=$access_token" >> tokens.txt
echo "EMAIL=$EMAIL" >> tokens.txt
echo "saved to tokens.txt"
