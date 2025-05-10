# load tokens from file
source ./tokens.txt

die() {
	echo >&2 "$@"
	exit 1
}

# quit if no params given
[ "$#" -eq 1 ] || die "required argument NEW_EMAIL, $# arg(s) provided"
NEW_EMAIL=$1

# get the response
response=$(curl \
	-H 'Content-Type: application/json' \
	-H "Authorization: Bearer $ACCESS" \
	--request PUT \
	--data "{\"new_email\": \"$NEW_EMAIL\"}" \
	http://localhost:8080/user/email)

# show the response to the user
echo $response | jq
