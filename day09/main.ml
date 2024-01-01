
module StrSet = Set.Make(String)

type dir = Up | Right | Down | Left

let signum x = match x with
  | x when x < 0 -> -1
  | x when x > 0 -> 1
  | _ -> 0;;

let adjust h t =
  let hx, hy = h in
  let tx, ty = t in
  if abs (hx - tx) > 1 || abs (hy - ty) > 1 then
    (tx + signum (hx - tx), ty + signum (hy - ty))
  else
    t;;

let move_one dir rope =
  let x, y = fst rope in
  let t = snd rope in
  let h = match dir with
  | Up -> (x, y-1)
  | Down -> (x, y+1)
  | Left -> (x-1, y)
  | Right -> (x+1, y) in
  let nt = adjust h t in
  (h, nt)
;;

let make_dir ch = match ch with
  | "R" -> Right
  | "L" -> Left
  | "U" -> Up
  | "D" -> Down
  | _ -> raise (Invalid_argument ("Invalid direction " ^ ch))
;;

let parse_move line =
  match String.split_on_char ' ' line with
  | [] | [_] -> raise (Invalid_argument ("Invalid param " ^ line))
  | hd::tl -> (make_dir hd, int_of_string (List.hd tl))
;;

let pp_move move =
  let m = match fst move with
  | Right -> "R"
  | Left -> "L"
  | Up -> "U"
  | Down -> "D" in
  Printf.sprintf "(%s %d)" m (snd move)
;;

let pp_pos pos =
  let x, y = pos in
  Printf.sprintf "(%d,%d)" x y
;;

let pp_rope rope =
  let h, t = rope in
  Printf.sprintf "(%s,%s)" (pp_pos h) (pp_pos t)
;;

let rec parse_lines ic acc =
  try
    match input_line ic with
    | "" -> parse_lines ic acc
    | line -> parse_move line :: parse_lines ic acc
  with End_of_file -> []
;;

let rec apply_n_times n f x =
  if n <= 0 then x else apply_n_times (n-1) f (f x)
;;

let move_with_set dir rope_and_set =
  let rope, set = rope_and_set in
  let new_rope = move_one dir rope in
  Printf.printf "%s\n" (pp_pos (snd new_rope));
  (new_rope, StrSet.add (pp_pos (snd new_rope)) set)
;;

let reduce rope_and_set move =
  let dir, n = move in
  apply_n_times n (move_with_set dir) rope_and_set
;;

let start = ((0, 0), (0, 0));;

let moves = parse_lines (open_in "input.txt") [] in
let init_set = StrSet.empty in
let rope, set = List.fold_left reduce (start, init_set) moves in
Printf.printf "Part 1: %d" (StrSet.cardinal set)
;;

