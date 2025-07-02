Get-Content env | ForEach-Object {
  $name,$val = $_ -split '=',2
  $Env:$name = $val
}
java -cp target\classes org.challange.Main $args