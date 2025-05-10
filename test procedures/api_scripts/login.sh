curl \
	-H 'Content-Type: application/json' \
	--request POST \
	--data "{\"email\": \"$EMAIL\"}" \
	http://localhost:8080/session/login
	
# store email so it can be used for future commands
echo "EMAIL=$EMAIL" > tokens.txt
