# Blog app demo in ClojureScript

Blogs are just markdown files in the `resources/md` folder, and any images used are in the `resources/public/img` folder. Therefore, no database setup is required. When you add a new blog, just update the `articles` data in the `src/cljs/db.cljs` file.

## Development Mode

### Run application:

```
lein do clean, figwheel
```

Browse to [http://localhost:3449](http://localhost:3449). Figwheel will automatically push cljs changes to the browser.

A screenshot of the app is below:

<img src="img/app-screenshot.png" width="100%" height="100%" />

## Deploy on Heroku:

1. Create [heroku free account](https://www.heroku.com)
2. Install [Heroku CLI](https://devcenter.heroku.com/articles/getting-started-with-clojure#set-up)
3. Login to Heroku: `heroku login`. Make sure you are in the app directory at the terminal.
4. Create a Heroku app: `heroku create`. You can see your created app in your Heroku account dashboard. Now, go to your heroku dashboard, and click on the app you created. Then go to the `settings` tab and check the `name` section - if you would like to edit, do it now.
5. If you haven't initialized `git` for you app, run `git init`.
6. Run `lein clean`
7. Add all of your app files for commit: `git add -A`. You can check the status of the files that will be added for commit by running `git status`.
8. Commit all files: `git commit -m "add some meaningful text here"`
9. Deploy to Heroku: `git push heroku master`
10. In order to push further changes, just redo steps 6, 7, 8, and 9 after any changes you make.

Now, you can see your app running on the url mentioned in the `Domains and certificates` of the `settings` page. As a shortcut, you could also run `heroku open`.

If you wish to keep your app in your personal git repo, you just have to add the remote url of your newly created git repo by running `git remote add origin <your-git-repo-url>`, and do the following:

  - Steps 6, 7, and 8 from above
  - Then, `git push -u origin master`. Rather than deploying to Heroku, you are pushing updates to your git repo.


## General Production Build (for any infrastructure that requires `uberjar`)

```
lein clean
lein with-profile prod uberjar
```

That should compile the clojurescript code first, and then create the standalone jar.

When you run the jar you can set the port the ring server will use by setting the environment variable PORT. If it's not set, it will run on port 3000 by default.
