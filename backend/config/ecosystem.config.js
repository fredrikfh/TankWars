// this is config for the pm2 process manager
module.exports = {
  apps: [
    {
      name: 'backend',
      script: 'dist/src/index.js',
      env: {
        NODE_ENV: 'development',
      },
      env_production: {
        NODE_ENV: 'production',
      },
    },
  ],
};
