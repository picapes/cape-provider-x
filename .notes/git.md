# Git Notes (Ignore if you don't know)

## Show remotes
`git remote -v`

## Add upstream
```
git remote add upstream https://github.com/litetex-oss/mcm-cape-provider.git
```

## Preview commits from
1. `upstream/dev`
```
git fetch upstream
git log HEAD..upstream/dev --oneline
```

2. `upstream/master`
```
git fetch upstream
git log HEAD..upstream/master --oneline
```

## Preview diff (actual code changes)
1. dev- `git diff HEAD..upstream/dev`
2. master- `git diff HEAD..upstream/master`

## Fetch/Merge updates from
1. `upstream/dev`
```
git fetch upstream
git checkout dev
git merge upstream/dev
```

2. `upstream/master`
```
git fetch upstream
git checkout master
git merge upstream/master
```