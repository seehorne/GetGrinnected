# make a request
curl \
	-H 'Content-Type: application/json' \
	--request POST \
	--data "{\"username\": \"manual_test\", \"email\": \"$EMAIL\"}" \
	http://localhost:8080/session/signup
	
# store email so it can be used for future commands
echo "EMAIL=$EMAIL" > tokens.txt