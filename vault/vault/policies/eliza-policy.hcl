path "kv/data/users/eliza/*" {
	capabilities = ["create", "update", "read"]
}

path "kv/delete/users/eliza/*" {
	capabilities = ["delete", "update"]
}

path "kv/undelete/users/eliza/*" {
	capabilities = ["update"]
}

path "kv/destroy/users/eliza/*" {
	capabilities = ["update"]
}

path "kv/metadata/users/eliza/*" {
	capabilities = ["list", "read", "delete"]
}

path "kv/metadata/" {
	capabilities = ["list"]
}

path "kv/metadata/users/" {
	capabilities = ["list"]
}

path "kv/data/shared/*" {
	capabilities = ["read"]
}
